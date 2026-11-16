package com.qveto.exam.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Question(
    Long id,
    String subjectTag,
    String questionText,
    String optionA,
    String optionB,
    String optionC,
    String optionD,
    OptionChoice correctOption,
    BigDecimal defaultMarks,
    boolean archived,
    Long createdBy,
    LocalDateTime createdAt
) {

}
