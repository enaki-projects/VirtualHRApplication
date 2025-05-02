package org.enaki.license.cv_db.business.business_models.create.iq;

import org.enaki.license.cv_db.persistence.entities.iq.IQTest;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BusinessCreateIQTest {
    public Long uid;
    public String testName;
    public Float exactIQ;
    public Integer iq;
    public Integer iqAdjustedByAge;
    public Integer timeTakenInSeconds;
    public String diagnostic;
    public String userAnswer;
    public Date submittedOn;

    public BusinessCreateIQTest(Long uid, String testName, Float exactIQ, Integer iq, Integer iqAdjustedByAge, Integer timeTakenInSeconds, String diagnostic, String userAnswer, Date submittedOn) {
        this.uid = uid;
        this.testName = testName;
        this.exactIQ = exactIQ;
        this.iq = iq;
        this.iqAdjustedByAge = iqAdjustedByAge;
        this.timeTakenInSeconds = timeTakenInSeconds;
        this.diagnostic = diagnostic;
        this.userAnswer = userAnswer;
        this.submittedOn = submittedOn;
    }

    public static IQTest toIQTest(BusinessCreateIQTest iqTest){
        return new IQTest(
                -1L,
                iqTest.uid,
                iqTest.testName,
                iqTest.exactIQ,
                iqTest.iq,
                iqTest.iqAdjustedByAge,
                iqTest.timeTakenInSeconds,
                iqTest.diagnostic,
                iqTest.userAnswer,
                iqTest.submittedOn,
                null
        );
    }

    public boolean isValid() {
        return (uid != null) && (testName != null) && (iq != null) && (timeTakenInSeconds != null) && (userAnswer != null) && (submittedOn != null);
    }

    public String getRequiredFieldsMessage(){
        return "IQTest require fields (uid, testName, iq, timeTakenInSeconds, userAnswers, submittedOn)";
    }

    public String getDuplicatedEntityMessage(){
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return String.format("IQTest with value fields (uid, testName, submittedOn): (%s, %s, %s) is existent",
                uid, testName, df1.format(submittedOn));
    }
}
