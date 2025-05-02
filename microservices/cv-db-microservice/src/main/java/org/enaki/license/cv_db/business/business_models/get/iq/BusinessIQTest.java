package org.enaki.license.cv_db.business.business_models.get.iq;

import org.enaki.license.cv_db.persistence.entities.iq.IQTest;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessIQTest {
    public Long iqTestId;
    public Long uid;
    public String testName;
    public Float exactIQ;
    public Integer iq;
    public Integer iqAdjustedByAge;
    public Integer timeTakenInSeconds;
    public String diagnostic;
    public String userAnswer;
    public String submittedOn;
    private static final DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public BusinessIQTest(Long iqTestId, Long uid, String testName, Float exactIQ, Integer iq, Integer iqAdjustedByAge, Integer timeTakenInSeconds, String diagnostic, String userAnswer, Date submittedOn) {
        this.iqTestId = iqTestId;
        this.uid = uid;
        this.testName = testName;
        this.exactIQ = exactIQ;
        this.iq = iq;
        this.iqAdjustedByAge = iqAdjustedByAge;
        this.timeTakenInSeconds = timeTakenInSeconds;
        this.diagnostic = diagnostic;
        this.userAnswer = userAnswer;
        this.submittedOn = df1.format(submittedOn);
    }

    public static BusinessIQTest toBusinessIQTest(IQTest iqTest){
        return new BusinessIQTest(
                iqTest.iqTestId,
                iqTest.uid,
                iqTest.testName,
                iqTest.exactIQ,
                iqTest.iq,
                iqTest.iqAdjustedByAge,
                iqTest.timeTakenInSeconds,
                iqTest.diagnostic,
                iqTest.userAnswer,
                iqTest.submittedOn
        );
    }

    public static List<BusinessIQTest> toBusinessIQTestList(List<IQTest> iqTestList){
        return iqTestList.stream()
                .map(BusinessIQTest::toBusinessIQTest)
                .collect(Collectors.toList());
    }
}
