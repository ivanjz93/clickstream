package org.dan.app;

import org.dan.entity.BaseRecord;
import org.dan.entity.LogAnalyzeJob;
import org.dan.utils.DateUtils;
import org.dan.utils.LogAnalyzeDao;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HalfCallBack implements Runnable {
    private LogAnalyzeDao dao;
    private JedisPool pool = new JedisPool("nn1", 6379);


    public HalfCallBack(){
        try {
            dao = new LogAnalyzeDao();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();

        //24:00，将缓存清空
        if(calendar.get(Calendar.MINUTE) == 0
                && calendar.get(Calendar.HOUR) == 24) {
            CacheData.setPvMap(new HashMap<String, Integer>());
            CacheData.setUvMap(new HashMap<String, Long>());
        }

        String date = DateUtils.getDate();
        List<BaseRecord> baseRecordList = getBaseRecords(date);
        List<BaseRecord> appendDataList = getAppData(baseRecordList);
        dao.saveHalfAppendRecord(appendDataList);
    }

    private List<BaseRecord> getBaseRecords(String date) {
        List<LogAnalyzeJob> jobList = dao.loadJobList();
        Jedis jedis = pool.getResource();
        List<BaseRecord> baseRecordList = new ArrayList<>();
        for(LogAnalyzeJob analyzeJob : jobList){
            String pvKey = "log:" + analyzeJob.getJobName() + ":pv:" + date;
            String uvKey = "log:" + analyzeJob.getJobName() + ":uv:" + date;
            String pv = jedis.get(pvKey);
            long uv = jedis.scard(uvKey);
            BaseRecord baseRecord = new BaseRecord(analyzeJob.getJobName(), Integer.parseInt(pv.trim()), uv);
            baseRecordList.add(baseRecord);
        }
        jedis.close();
        return baseRecordList;
    }

    private List<BaseRecord> getAppData(List<BaseRecord> baseRecordList) {
        List<BaseRecord> appendDataList = new ArrayList<>();
        for(BaseRecord baseRecord : baseRecordList) {
            int pvAppendValue = CacheData.getPv(baseRecord.getPv(), baseRecord.getIndexName());
            long uvAppendValue = CacheData.getUv(baseRecord.getUv(), baseRecord.getIndexName());
            appendDataList.add(new BaseRecord(baseRecord.getIndexName(), pvAppendValue, uvAppendValue));
        }
        return appendDataList;
    }
}
