package org.dan.entity;

public class LogAnalyzeJob {
    private int jobId;
    private String jobName;
    private int jobType;
    private int businessId;
    private int status;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LogAnalyzeJob{" +
                "jobId=" + jobId +
                ", jobName='" + jobName + '\'' +
                ", jobType=" + jobType +
                ", businessId=" + businessId +
                ", status=" + status +
                '}';
    }
}
