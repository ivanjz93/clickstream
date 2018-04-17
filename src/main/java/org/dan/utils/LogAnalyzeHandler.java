package org.dan.utils;

import com.google.gson.Gson;
import org.dan.entity.LogAnalyzeJob;
import org.dan.entity.LogAnalyzeJobDetail;
import org.dan.entity.LogMessage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogAnalyzeHandler {

    private static JedisPool pool = new JedisPool("nn1", 6379);
    private static boolean reloaded = false;

    private static LogAnalyzeDao logAnalyzeDao;

    //用来保存job信息，key为jobType，value为该类别下所有的任务
    private static Map<String, List<LogAnalyzeJob>> jobMap;
    //用来保存job的判断条件，key为jobId，value为list，list 中封装了判断条件
    private static Map<String, List<LogAnalyzeJobDetail>> jobDetail;

    static {
        try {
            logAnalyzeDao = new LogAnalyzeDao();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            System.exit(1);
        }
        jobMap = loadJobMap();
        jobDetail = loadJobDetailMap();
    }

    public static void process(LogMessage logMessage) {
        if(jobMap == null || jobDetail == null) {
            reloadDataModel();
        }

        List<LogAnalyzeJob> analyzeJobList = jobMap.get(Integer.toString(logMessage.getType()));
        for(LogAnalyzeJob logAnalyzeJob : analyzeJobList) {
            boolean isMatch = false;
            List<LogAnalyzeJobDetail> logAnalyzeJobDetailList = jobDetail.get(Integer.toString(logAnalyzeJob.getJobId()));
            if(logAnalyzeJobDetailList != null) {
                for(LogAnalyzeJobDetail logAnalyzeJobDetail : logAnalyzeJobDetailList){
                    String fieldValueInLog = logMessage.getCompareFieldValue(logAnalyzeJobDetail.getField());
                    if(logAnalyzeJobDetail.getCompare() == 1 && fieldValueInLog.contains(logAnalyzeJobDetail.getValue())){
                        isMatch = true;
                    } else if(logAnalyzeJobDetail.getCompare() == 2 && fieldValueInLog.equals(logAnalyzeJobDetail.getValue())) {
                        isMatch = true;
                    } else {
                        isMatch = false;
                    }
                    if(!isMatch)
                        break;
                }
            }
            if(isMatch) {
                String pvKey = "log:" + logAnalyzeJob.getJobName() + ":pv:" + DateUtils.getDate();
                String uvKey = "log:" + logAnalyzeJob.getJobName() + ":uv:" + DateUtils.getDate();
                Jedis jedis = pool.getResource();
                jedis.incr(pvKey);
                jedis.sadd(uvKey, logMessage.getUserName());
                jedis.close();
            }
        }

    }

    public static LogMessage parser(String line) {
        LogMessage logMessage = new Gson().fromJson(line, LogMessage.class);
        return logMessage;
    }

    public static boolean isValidType(int jobType) {
        if(jobType == LogTypeConstant.BUY || jobType == LogTypeConstant.CLICK
                || jobType == LogTypeConstant.VIEW || jobType == LogTypeConstant.SEARCH) {
            return true;
        }
        return false;
    }

    public static void scheduleLoad() {
        String date = DateUtils.getDateTime();
        int now = Integer.parseInt(date.split(":")[1]);
        if(now % 10 == 0) {
            reloadDataModel();
        } else {
            reloaded = true;
        }
    }

    private static synchronized void reloadDataModel() {
        if(reloaded) {
            jobMap = loadJobMap();
            jobDetail = loadJobDetailMap();
            reloaded = false;
        }
    }

    private static Map<String,List<LogAnalyzeJob>> loadJobMap() {
        Map<String,List<LogAnalyzeJob>> map = new HashMap<>();
        List<LogAnalyzeJob> logAnalyzeJobList = logAnalyzeDao.loadJobList();
        for(LogAnalyzeJob job : logAnalyzeJobList) {
            int jobType = job.getJobType();
            List<LogAnalyzeJob> jobList = map.get(Integer.toString(jobType));
            if(jobList == null) {
                jobList = new ArrayList<>();
                map.put(Integer.toString(jobType), jobList);
            }
            jobList.add(job);
        }
        return map;
    }


    private static Map<String, List<LogAnalyzeJobDetail>> loadJobDetailMap() {
        Map<String, List<LogAnalyzeJobDetail>> map = new HashMap<>();
        List<LogAnalyzeJobDetail> logAnalyzeJobDetailList = logAnalyzeDao.loadJobDetailList();
        for(LogAnalyzeJobDetail logAnalyzeJobDetail : logAnalyzeJobDetailList) {
            int jobId = logAnalyzeJobDetail.getJobId();
            List<LogAnalyzeJobDetail> jobDetails = map.get(Integer.toString(jobId));
            if(jobDetails == null) {
                jobDetails = new ArrayList<>();
                map.put(Integer.toString(jobId), jobDetails);
            }
            jobDetails.add(logAnalyzeJobDetail);
        }
        System.out.println("jobDetailMap: " + map);
        return map;
    }
}
