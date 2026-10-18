import json
import os

from flask import Flask
from flask import Response, request, abort

import pyspark  # noqa: F401  (import kept so a missing PySpark fails fast at boot)
from pyspark.sql import SparkSession
from pyspark.sql.functions import col, current_date, datediff, floor, to_date

app = Flask(__name__)

MAX_AGE_DEFAULT = 1000
MIN_AGE_DEFAULT = 0
KEYWORDS_DEFAULT = []
HEADERS = {'Content-type': 'application/json'}

# Spark master. Defaults to in-process local[1] so the service runs standalone
# with no separate Spark cluster. Point SPARK_MASTER at spark://spark-master:7077
# only if the optional standalone cluster is deployed.
SPARK_MASTER = os.environ.get('SPARK_MASTER', 'local[1]')
SPARK_EXECUTOR_MEMORY = os.environ.get('SPARK_EXECUTOR_MEMORY', '512m')

_spark = None


def get_spark():
    """Lazily create and reuse a single SparkSession per worker process."""
    global _spark
    if _spark is None:
        _spark = (
            SparkSession.builder
            .appName("cvs-filter-app")
            .master(SPARK_MASTER)
            .config("spark.executor.memory", SPARK_EXECUTOR_MEMORY)
            .config("spark.ui.enabled", "false")
            .getOrCreate()
        )
    return _spark


def iterate_nested(s):
    if isinstance(s, (list, tuple)):
        for value in s:
            yield from iterate_nested(value)
    elif isinstance(s, dict):
        for value in s.values():
            yield from iterate_nested(value)
    elif isinstance(s, set):
        for value in s:
            yield from iterate_nested(value)
    elif isinstance(s, str):
        yield s.lower()


def process_cvs(cvs, keywords=KEYWORDS_DEFAULT, min_age=MIN_AGE_DEFAULT, max_age=MAX_AGE_DEFAULT):
    spark = get_spark()
    df = spark.read.json(spark.sparkContext.parallelize([json.dumps(cvs)]))
    result = list(map(
        lambda item: item.asDict(recursive=True),
        df.withColumn('age', floor(datediff(current_date(), to_date(col('birthdate'), 'yyyy-MM-dd')) / 365.25))
          .where((col("age") >= min_age) & (col("age") <= max_age))
          .rdd
          .filter(lambda x: all(item.lower() in ' '.join(list(iterate_nested(x))) for item in keywords))
          .collect()
    ))
    return result


@app.route("/")
def hello():
    return "Hello World!"


@app.route("/health")
def health():
    return Response(response=json.dumps({"status": "ok", "master": SPARK_MASTER}),
                    headers=HEADERS, status=200)


@app.route("/filter-cvs", methods=['POST'])
def filter_cvs():
    cv_data = request.json
    if "cvs" not in cv_data:
        abort(400, "Cvs not specified")
    min_age = cv_data.get("min_age", MIN_AGE_DEFAULT)
    max_age = cv_data.get("max_age", MAX_AGE_DEFAULT)
    keywords = cv_data.get("keywords", KEYWORDS_DEFAULT)
    data_dict = process_cvs(cv_data["cvs"], keywords, min_age, max_age)
    return Response(response=json.dumps(data_dict).encode(), headers=HEADERS, status=200)


if __name__ == '__main__':
    # Local development only; containers serve via gunicorn (see Dockerfile).
    port = int(os.environ.get('PORT', 2013))
    app.run(debug=True, host='127.0.0.1', port=port)
