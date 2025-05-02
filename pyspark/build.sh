# -- Software Stack Version

SPARK_VERSION="3.0.0"
HADOOP_VERSION="2.7"
JUPYTERLAB_VERSION="2.1.5"

# -- Building the Images

docker build /
  -f cluster_base/cluster_base_dockerfile /
  -t cluster-base .

docker build /
  --build-arg spark_version="${SPARK_VERSION}" /
  --build-arg hadoop_version="${HADOOP_VERSION}" /
  -f spark_base/spark_base_dockerfile ^
  -t spark-base .

docker build /
  -f spark_master/spark_master_dockerfile /
  -t spark-master .

docker build /
  -f spark_worker/spark_worker_dockerfile /
  -t spark-worker .
  
docker build \
  --build-arg spark_version="${SPARK_VERSION}" \
  --build-arg jupyterlab_version="${JUPYTERLAB_VERSION}" \
  -f jupyterlab.jupyterlab_dockerfile \
  -t jupyterlab .
