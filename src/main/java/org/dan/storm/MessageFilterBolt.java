package org.dan.storm;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.dan.entity.LogMessage;
import org.dan.utils.LogAnalyzeHandler;

import java.util.Map;

public class MessageFilterBolt extends BaseRichBolt {
    private OutputCollector collector;
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String line = input.getString(0);
        System.out.println("receive log: " + line);
        LogMessage logMessage = LogAnalyzeHandler.parser(line);
        if(logMessage == null || !LogAnalyzeHandler.isValidType(logMessage.getType())) {
            return;
        }

        collector.emit(new Values(logMessage.getType(), logMessage));

        LogAnalyzeHandler.scheduleLoad();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("type", "message"));
    }
}
