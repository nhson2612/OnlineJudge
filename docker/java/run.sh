#!/bin/bash
# docker/java/run.sh

# Kiểm tra tham số đầu vào
if [ $# -lt 2 ]; then
    echo "❌ Usage: $0 <source_file.java> <input_file>"
    exit 1
fi

SOURCE_FILE=$1
INPUT_FILE=$2
OUTPUT_FILE="output.txt"

# Kiểm tra file tồn tại
if [ ! -f "$SOURCE_FILE" ]; then
    echo "❌ Source file not found: $SOURCE_FILE"
    exit 1
fi

if [ ! -f "$INPUT_FILE" ]; then
    echo "❌ Input file not found: $INPUT_FILE"
    exit 1
fi

# Lấy tên class từ file .java
CLASS_NAME=$(basename "$SOURCE_FILE" .java)

# Biên dịch Java
echo "🔧 Compiling $SOURCE_FILE..."
javac "$SOURCE_FILE"
COMPILE_EXIT=$?

if [ $COMPILE_EXIT -ne 0 ]; then
    echo "❌ Compilation Error"
    exit 1
fi

echo "✅ Compilation successful"

# Chạy chương trình với timeout
echo "🏃 Running program..."
timeout 5s java "$CLASS_NAME" < "$INPUT_FILE" > "$OUTPUT_FILE" 2>&1
EXIT_CODE=$?

if [ $EXIT_CODE -eq 124 ]; then
    echo "❌ Time Limit Exceeded (>5s)"
    exit 124
elif [ $EXIT_CODE -ne 0 ]; then
    echo "❌ Runtime Error (exit code $EXIT_CODE)"
    cat "$OUTPUT_FILE" 2>/dev/null
    exit $EXIT_CODE
else
    echo "✅ Program executed successfully"
    if [ -f "$OUTPUT_FILE" ]; then
        echo "📄 Output:"
        cat "$OUTPUT_FILE"
    fi
fi

exit 0
