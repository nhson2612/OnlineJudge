FROM gcc:13

WORKDIR /app

COPY run.sh .
RUN chmod +x ./run.sh \
    && apt-get update \
    && apt-get install -y time \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/app/run.sh"]