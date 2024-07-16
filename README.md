# java-http-server

```bash
docker build -t c100k .
docker run --rm -it --network=host c100k --http-port=22221
```

```bash
docker run --rm -it qyvlik/wrk -t 2 -c 128 -d 30s http://172.31.40.214:22221
```

---

# ref

1. https://github.com/ebarlas/java-httpserver-vthreads
2. https://github.com/smallnest/C1000K-Servers


