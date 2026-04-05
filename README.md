# java-http-server

```bash
docker build -t c100k .
docker run --rm -it --network=host c100k --http-port=22222
```

```bash
docker run --rm -it --network=host qyvlik/wrk -t 4 -c 128 -d 30s --latency http://127.0.0.1:22222
```

---

# ref

1. https://github.com/ebarlas/java-httpserver-vthreads
2. https://github.com/smallnest/C1000K-Servers
3. https://raby.sh/1m-http-requests-per-second-using-nginx-and-ubuntu-1204-on-ec2.html

## native-image

```shell
java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image \
  -jar \
  target/c100k-server-0.1.2.jar
```
