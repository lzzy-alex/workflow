package com.ucap.workflow.entity;

public class Step extends Base {

    private static final long serialVersionUID = -3318253389438392605L;
    public String code;
    public String name;
    public int tpl_id;
    public String biz_code;
    public int seq_no;

    public Step(String biz_code, int tpl_id, String code, String name, int seq_no) {
        super();
        this.code = code;
        this.name = name;
        this.tpl_id = tpl_id;
        this.biz_code = biz_code;
        this.seq_no = seq_no;
    }

    public Step() {

    }

}
