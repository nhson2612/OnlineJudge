#!/bin/bash
# docker/java/run.sh

set -e

# Constants
TIMEOUT_SECONDS=5
EXIT_CODE_TIMEOUT=124
EXIT_CODE_COMPILATION_ERROR=1
JAVA_CLASS="Main"
OUTPUT_FILE=""

# Check arguments
if [ $# -lt 3 ]; then
    echo "❌ Error: Missing arguments" > "out_unknown.txt"
    echo "Usage: $0 <source_file> <input_file> <order_index>" >> "out_unknown.txt"
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

# Clean up previous runs
rm -f "$OUTPUT_FILE" "${JAVA_CLASS}.class" mem_usage.txt stderr.txt

# Compile Java
if ! javac "$SOURCE_FILE" 2> "$OUTPUT_FILE"; then
    echo "❌ Compilation Error" >> "$OUTPUT_FILE"
    exit $EXIT_CODE_COMPILATION_ERROR
fi

# Run Java program with timeout and memory limits
if ! timeout --signal=SIGKILL "$TIMEOUT_SECONDS"s \
    /usr/bin/time -f "%M" -o mem_usage.txt \
    java "$JAVA_CLASS" < "$INPUT_FILE" > "$OUTPUT_FILE" 2> stderr.txt; then
    EXIT_CODE=$?
    if [ $EXIT_CODE -eq $EXIT_CODE_TIMEOUT ]; then
        echo "❌ Time Limit Exceeded (>${TIMEOUT_SECONDS}s)" >> "$OUTPUT_FILE"
        exit $EXIT_CODE_TIMEOUT
    else
        echo "❌ Runtime Error (exit code $EXIT_CODE)" >> "$OUTPUT_FILE"
        cat stderr.txt >> "$OUTPUT_FILE" 2>/dev/null
        exit $EXIT_CODE
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
rm -f "${JAVA_CLASS}.class" mem_usage.txt stderr.txt
exit 0
