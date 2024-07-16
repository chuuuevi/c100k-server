# java-http-server

```bash
docker build -t c100k .
docker run --rm -it --network=host c100k --http-port=22221 --server-type=jdk21
```

```bash
docker run --rm -it qyvlik/wrk -t 2 -c 128 -d 30s http://172.31.15.175:22221
```

---

# ref

1. https://github.com/ebarlas/java-httpserver-vthreads
2. https://github.com/smallnest/C1000K-Servers
3. https://raby.sh/1m-http-requests-per-second-using-nginx-and-ubuntu-1204-on-ec2.html


