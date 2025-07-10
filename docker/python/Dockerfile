FROM python:3.11-slim

WORKDIR /app

COPY run.sh .

RUN chmod +x ./run.sh \
    && apt-get update \
    && apt-get install -y time \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/app/run.sh"]
