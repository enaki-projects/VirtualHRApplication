package org.enaki.license.cv_db.persistence.entities.iq;

import java.sql.Date;

public class IQTest {
    public Long iqTestId;
    public Long uid;
    public String testName;
    public Float exactIQ;
    public Integer iq;
    public Integer iqAdjustedByAge;
    public Integer timeTakenInSeconds;
    public String diagnostic;
    public String userAnswer;
    public Date submittedOn;
    public String description;

    public IQTest(Long iqTestId, Long uid, String testName, Float exactIQ, Integer iq, Integer iqAdjustedByAge, Integer timeTakenInSeconds, String diagnostic, String userAnswer, Date submittedOn, String description) {
        this.iqTestId = iqTestId;
        this.uid = uid;
        this.testName = testName;
        this.exactIQ = exactIQ;
        this.iq = iq;
        this.iqAdjustedByAge = iqAdjustedByAge;
        this.timeTakenInSeconds = timeTakenInSeconds;
        this.diagnostic = diagnostic;
        this.userAnswer = userAnswer;
        this.submittedOn = submittedOn;
        this.description = description;
    }
}
