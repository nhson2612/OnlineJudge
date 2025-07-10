#!/bin/bash
# docker/python/run.sh

set -e

# Constants
TIMEOUT_SECONDS=5
EXIT_CODE_TIMEOUT=124
EXIT_CODE_RUNTIME_ERROR=1
OUTPUT_FILE=""

# Check arguments
if [ $# -lt 3 ]; then
    echo "❌ Error: Missing arguments" > "out_unknown.txt"
    echo "Usage: $0 <source_file.py> <input_file> <order_index>" >> "out_unknown.txt"
    exit 1
fi

SOURCE_FILE="$1"
INPUT_FILE="/input/$2"
ORDER_INDEX="$3"
OUTPUT_FILE="out${ORDER_INDEX}.txt"

# Validate files
if [ ! -f "$SOURCE_FILE" ]; then
    echo "❌ Error: Source file not found: $SOURCE_FILE" > "$OUTPUT_FILE"
    exit 1
fi

if [ ! -f "$INPUT_FILE" ]; then
    echo "❌ Error: Input file not found: $INPUT_FILE" > "$OUTPUT_FILE"
    exit 1
fi

# Clean up
rm -f "$OUTPUT_FILE" mem_usage.txt stderr.txt

# Run Python script with timeout and memory limit
if ! timeout --signal=SIGKILL "$TIMEOUT_SECONDS"s \
    /usr/bin/time -f "%M" -o mem_usage.txt \
    python3 "$SOURCE_FILE" < "$INPUT_FILE" > "$OUTPUT_FILE" 2> stderr.txt; then
    EXIT_CODE=$?
    if [ $EXIT_CODE -eq $EXIT_CODE_TIMEOUT ]; then
        echo "❌ Time Limit Exceeded (>${TIMEOUT_SECONDS}s)" >> "$OUTPUT_FILE"
        exit $EXIT_CODE_TIMEOUT
    else
        echo "❌ Runtime Error (exit code $EXIT_CODE)" >> "$OUTPUT_FILE"
        cat stderr.txt >> "$OUTPUT_FILE" 2>/dev/null
        exit $EXIT_CODE_RUNTIME_ERROR
    fi
fi

# Check memory usage
MAX_MEMORY_KB=512000
ACTUAL_MEMORY=$(cat mem_usage.txt)
if [ "$ACTUAL_MEMORY" -gt "$MAX_MEMORY_KB" ]; then
    echo "❌ Memory Limit Exceeded (${ACTUAL_MEMORY}KB > ${MAX_MEMORY_KB}KB)" >> "$OUTPUT_FILE"
    exit 1
fi

# Clean up
rm -f mem_usage.txt stderr.txt
exit 0
