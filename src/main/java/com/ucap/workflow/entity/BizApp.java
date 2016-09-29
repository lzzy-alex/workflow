package com.ucap.workflow.entity;

public class BizApp extends Base {

    private static final long serialVersionUID = -1496857102556820813L;
    public String app_code;
    public String biz_code;
    public String step_code;
    public int status;
    
    public BizApp(String app_code, String biz_code, String step_code, int status) {
        super();
        this.app_code = app_code;
        this.biz_code = biz_code;
        this.step_code = step_code;
        this.status = status;
    }

    public static enum Status {
        pending(1, "审核中"), ret_mod(2, "退回修改"), passed(3, "已通过"), closed(4, "已终止"), other(5, "其它");
        private String n;
        private int v;
        private Status(int v, String n) {
            this.v = v;
            this.n = n;
        }
        public int val() {
            return this.v;
        }
        public String toString() {
            return this.n;
        }
        public static Status getByVal(int v) {
            switch ( v ) {
            case 1: return pending;
            case 2: return ret_mod;
            case 3: return passed;
            case 4: return closed;
            default:return other;
            }
        }
    }

}
