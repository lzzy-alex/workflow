package com.ucap.workflow.entity;

public class TplCtrl extends Base {

    private static final long serialVersionUID = -8898766971118260711L;
    public int tpl_id;
    public String name;
    public String type;
    public String extra_values;
    public int seq_no;
    public String style;
    public boolean require;
    public String regex;
    public String access_role;
    public String access_right;

    public TplCtrl() {
    }

    public TplCtrl(String name, String type, int seq_no) {
        this.name = name;
        this.type = type;
        this.seq_no = seq_no;
    }

    // not mapping field
    public boolean readonly;
    public String biz_code;
    public String app_code;
    public TplCtrlDat ctrl_dat = new TplCtrlDat();

}
