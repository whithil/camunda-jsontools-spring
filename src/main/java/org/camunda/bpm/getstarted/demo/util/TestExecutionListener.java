package org.camunda.bpm.getstarted.demo.util;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

/**
 * @author Daniel Meyer
 *
 */
@Service
public class TestExecutionListener implements ExecutionListener {

    public static String pid = "";
    public static String bk = "";
    public static String exec = "";
    public static String act = "";

    public void notify(DelegateExecution execution) throws Exception {
        pid = execution.getProcessInstanceId();
        bk = execution.getBusinessKey();
        exec = execution.getId();
        act = execution.getCurrentActivityId();
    }

}