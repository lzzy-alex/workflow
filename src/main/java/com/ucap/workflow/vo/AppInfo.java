package com.ucap.workflow.vo;

import java.util.ArrayList;
import java.util.List;

import com.ucap.workflow.entity.Step;
import com.ucap.workflow.entity.TplCtrl;

public class AppInfo {

    public Step curStep = null;
    public List<Step> steps = new ArrayList<Step>();
    public List<TplCtrl> ctrls = new ArrayList<TplCtrl>();

}
