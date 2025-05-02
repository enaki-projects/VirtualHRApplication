from flask import Flask
from flask import Response, request, abort
import json

app = Flask(__name__)

MAX_AGE_DEFAULT = 1000
MIN_AGE_DEFAULT = 0
KEYWORDS_DEFAULT = []
HEADERS = {'Content-type': 'application/json'}

import pyspark
from pyspark.sql import SparkSession
from pyspark.sql import SQLContext
from pyspark.sql.types import *
from pyspark.sql.functions import *


import json
#df = spark.read.json("cvs.json", multiLine=True)
#df.printSchema()
#df.show(n=1)

def iterate_nested(s):
    if isinstance(s, (list, tuple)):
        for index, value in enumerate(s):
            yield from iterate_nested(value)
    elif isinstance(s, dict):
        for key, value in s.items():
            yield from iterate_nested(value)
    elif isinstance(s, set):
        for value in s:
            yield from iterate_nested(value)
    elif isinstance(s, str):
        yield s.lower()

        
def process_cvs(cvs, keywords=KEYWORDS_DEFAULT, min_age=MIN_AGE_DEFAULT, max_age=MAX_AGE_DEFAULT):
    spark = SparkSession.\
        builder.\
        appName("cvs-filter-app").\
        master("spark://spark-master:7077").\
        config("spark.executor.memory", "512m").\
        getOrCreate()
    
    df = spark.read.json(spark.sparkContext.parallelize([json.dumps(cvs)]))
    result = list(map(lambda item: item.asDict(recursive=True), 
             df.withColumn('age', floor(datediff(current_date(), to_date(col('birthdate'), 'yyyy-MM-dd'))/365.25))
             .where((col("age") >= min_age) & (col("age") <= max_age))
             .rdd
             .filter(lambda x: all([item.lower() in ' '.join(list(iterate_nested(x))) for item in keywords]))
             .collect()))
    return result

@app.route("/")
def hello():
    return "Hello World!"

@app.route("/filter-cvs", methods=['POST'])
def filter_cvs():
    cv_data = request.json
    if "cvs" not in cv_data:
        abort(400, "Cvs not specified")
    min_age = cv_data["min_age"] if "min_age" in cv_data else MIN_AGE_DEFAULT
    max_age = cv_data["max_age"] if "max_age" in cv_data else MAX_AGE_DEFAULT
    keywords = cv_data["keywords"] if "keywords" in cv_data else KEYWORDS_DEFAULT
    data_dict = process_cvs(cv_data["cvs"], keywords, min_age, max_age)
    return Response(response=json.dumps(data_dict).encode(), headers=HEADERS, status=200)


if __name__ == '__main__':
    app.run(debug=True,host='0.0.0.0',port=2013)