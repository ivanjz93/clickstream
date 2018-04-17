package org.dan.utils;

import org.dan.entity.BaseRecord;
import org.dan.entity.LogAnalyzeJob;
import org.dan.entity.LogAnalyzeJobDetail;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class LogAnalyzeDao {
    private JdbcTemplate jdbcTemplate;

    public LogAnalyzeDao() throws PropertyVetoException {
        jdbcTemplate = new JdbcTemplate(DataSourceUtil.getDataSource());
    }


    public List<LogAnalyzeJob> loadJobList() {
        String sql = "select * from log_analyze_job";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<LogAnalyzeJob>(LogAnalyzeJob.class));
    }

    public List<LogAnalyzeJobDetail> loadJobDetailList() {
        String sql = "select * from log_analyze_job_condition";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<LogAnalyzeJobDetail>(LogAnalyzeJobDetail.class));
    }

    public int[][] saveMinuteAppendRecord(List<BaseRecord> appendDataList) {
        String sql = "INSERT INTO log_analyze_job_minute_append" +
                "(indexName, pv, uv, executeTime, createTime)" +
                "VALUES (?,?,?,?,?)";
        return saveAppendRecord(appendDataList, sql);
    }

    public int[][] saveHalfAppendRecord(List<BaseRecord> appendDataList) {
        String sql = "INSERT INTO log_analyze_job_half_append" +
                "(indexName, pv, uv, executeTime, createTime)" +
                "VALUES (?,?,?,?,?)";
        return saveAppendRecord(appendDataList, sql);
    }

    public int[][] saveHourAppendRecord(List<BaseRecord> appendDataList) {
        String sql = "INSERT INTO log_analyze_job_hour_append" +
                "(indexName, pv, uv, executeTime, createTime)" +
                "VALUES (?,?,?,?,?)";
        return saveAppendRecord(appendDataList, sql);
    }



    private int[][] saveAppendRecord(List<BaseRecord> appendDataList, String sql) {
        return jdbcTemplate.batchUpdate(sql, appendDataList, appendDataList.size(),
                (ps, r)->{
                    ps.setString(1, r.getIndexName());
                    ps.setInt(2,r.getPv());
                    ps.setLong(3, r.getUv());
                    ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                    ps.setTimestamp(5, new Timestamp(new Date().getTime()));
            }
        );
    }

    public static void main(String[] args) throws PropertyVetoException {
        LogAnalyzeDao dao = new LogAnalyzeDao();
        System.out.println(dao.loadJobList());
        System.out.println(dao.loadJobDetailList());
    }

}
