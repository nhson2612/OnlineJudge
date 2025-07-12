package org.example.judge.submission.utils;

import org.example.judge.core.domain.ProblemSubmission;
import org.example.judge.submission.model.dto.ProblemSubmissionReq;

import java.util.Random;

public class ProblemSubmissionFactory {

    private static final Random RANDOM = new Random();

    private static final String source1 =
            "#include <iostream>\n" +
            "#include <string>\n" +
            "using namespace std;\n" +
            "\n" +
            "int main() {\n" +
            "    int a,b;\n" +
            "    cin >> a >> b;\n" +
            "    cout << a + b;\n" +
            "    return 0;\n" +
            "}\n";
    private static final String source2 =
            "#include <iostream>\n" +
            "using namespace std;\n" +
            "\n" +
            "int main() {\n" +
            "    int a,b;\n" +
            "    cin >> a >> b;\n" +
            "    cout << a - b;\n" +
            "    return 0;\n" +
            "}\n";

    public ProblemSubmission createProblemSubmission(Long submissionId) {
        return null;
    }

    public static ProblemSubmissionReq createProblemSubmissionReq() {
        boolean random = RANDOM.nextBoolean();
        String source = random ? source1 : source2;
        String language = "cpp";
        Long problemId = random ? 1L : 2L;
        return new ProblemSubmissionReq(source, language, problemId);
    }

    public static ProblemSubmissionReq createProblemSubmissionReq_withNoLanguage() {
        boolean random = RANDOM.nextBoolean();
        String source = random ? source1 : source2;
        Long problemId = random ? 1L : 2L;
        return new ProblemSubmissionReq(source, null, problemId);
    }

    public static ProblemSubmissionReq createProblemSubmissionReq_withNoSource() {
        boolean random = RANDOM.nextBoolean();
        String language = random ? "cpp" : "java";
        Long problemId = random ? 1L : 2L;
        return new ProblemSubmissionReq(null, language, problemId);
    }

    public static ProblemSubmissionReq createProblemSubmissionReq_withNoProblemId() {
        boolean random = RANDOM.nextBoolean();
        String source = random ? source1 : source2;
        String language = random ? "cpp" : "java";
        return new ProblemSubmissionReq(source, language, null);
    }

    public static ProblemSubmissionReq createProblemSubmissionReq_withInvalidLanguage() {
        boolean random = RANDOM.nextBoolean();
        String source = random ? source1 : source2;
        Long problemId = random ? 1L : 2L;
        return new ProblemSubmissionReq(source, "invalid_language", problemId);
    }
}