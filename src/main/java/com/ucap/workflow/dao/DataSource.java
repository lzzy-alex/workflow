package com.ucap.workflow.dao;

import static com.ucap.toolkit.type.BoolUtil.toBool;
import static com.ucap.toolkit.type.CollectionUtil.isEmpty;
import static com.ucap.toolkit.type.IntUtil.toInt;
import static com.ucap.toolkit.type.StringUtil.str;
import static com.ucap.workflow.util.Toolkit.jdbc;

import java.util.Arrays;
import java.util.List;

import com.ucap.workflow.entity.BizApp;
import com.ucap.workflow.entity.Step;
import com.ucap.workflow.entity.Tpl;
import com.ucap.workflow.entity.TplCtrl;
import com.ucap.workflow.vo.AppInfo;

public class DataSource {

    public static void addTpl(String name) {
        String sql = "insert into wf_tpl(name) values(?)";
        jdbc().update( sql, Arrays.asList( name ) );
    }

    private static Tpl findTpl(int id) {
        String sql = "select id, name from wf_tpl where id=?";
        for ( Object [] row : jdbc().list( sql, Arrays.asList( id ), 2 ) ) {
            Tpl tpl = new Tpl();
            tpl.id = toInt( row[0] );
            tpl.name = str( row[1] );
            return tpl;
        }
        return null;
    }

    private static TplCtrl findTplCtrl(int id) {
        String sql = "select id, tpl_id,name,type,extra_values,seq_no,style,require,regex,access_role,access_right from wf_tpl_ctrl where id=?";
        for ( Object [] row : jdbc().list( sql, Arrays.asList( id ), 11 ) ) {
            TplCtrl c = new TplCtrl();
            c.id = toInt( row[0] );
            c.tpl_id = toInt( row[1] );
            c.name = str( row[2] );
            c.type = str( row[3] );
            c.extra_values = str( row[4] );
            c.seq_no = toInt( row[5] );
            c.style = str( row[6] );
            c.require = toBool( str( row[7] ) );
            c.regex = str( row[8] );
            c.access_role = str( row[9] );
            c.access_right = str( row[10] );
            return c;
        }
        return null;
    }

    public static void addOrUpdateTplCtrl(int tpl_id, List<TplCtrl> ctrls) {
        if ( isEmpty( ctrls ) || findTpl( tpl_id ) == null ) return;
        for ( TplCtrl c : ctrls ) {
            // new ctrl
            if ( c.id == 0 ) {
                String sql = "insert into wf_tpl_ctrl(tpl_id,name,type,extra_values,seq_no,style,require,regex,access_role,access_right) values(?,?,?,?,?,?,?,?,?,?)";
                jdbc().update( sql, Arrays.asList( tpl_id, c.name, c.type, c.extra_values, c.seq_no, c.style, c.require, c.regex, c.access_role, c.access_right ) );
            }
            // update ctrl
            else {
                if ( findTplCtrl( c.id ) == null ) continue;
                String sql = "update wf_tpl_ctrl set name=?,type=?,extra_values=?,seq_no=?,style=?,require=?,regex=?,access_role=?,access_right=? where tpl_id=? and id=?";
                jdbc().update( sql, Arrays.asList( c.name, c.type, c.extra_values, c.seq_no, c.style, c.require, c.regex, c.access_role, c.access_right, tpl_id, c.id ) );
            }
        }
    }

    public static void addBiz(String code, String name) {
        String sql = "insert into wf_biz(code, name) values(?,?)";
        jdbc().update( sql, Arrays.asList( code, name ) );
    }

    public static void addStep(Step s) {
        String sql = "insert into wf_step(tpl_id, biz_code, code, name, seq_no) values(?,?,?,?,?)";
        jdbc().update( sql, Arrays.asList( s.tpl_id, s.biz_code, s.code, s.name, s.seq_no ) );
    }

    public static void addBizApp(BizApp a) {
        String sql = "insert into wf_biz_app(app_code, biz_code, step_code, status) values(?,?,?,?)";
        jdbc().update( sql, Arrays.asList( a.app_code, a.biz_code, a.step_code, a.status ) );
    }

    public static String queryStatusByAppCode(String app_code) {
        String sql = "select s.name, a.status from wf_biz_app a left join wf_step s on a.step_code=s.code where a.app_code=?";
        for ( Object [] row : jdbc().list( sql, Arrays.asList( app_code ), 2 ) ) {
            String stepName = str( row[0] );
            String status = BizApp.Status.getByVal( toInt( row[1] ) ).toString();
            return stepName + " " + status;
        }
        return null;
    }

    private static Step getCurStep(String app_code) {
        String sql = "select s.id, s.tpl_id, s.biz_code, s.code, s.name, s.seq_no from wf_biz_app a"//
                + " left join wf_step s on a.step_code = s.code where a.app_code =? ";
        for ( Object [] row : jdbc().list( sql, Arrays.asList( app_code ), 6 ) ) {
            Step s = new Step();
            s.id = toInt( row[0] );
            s.tpl_id = toInt( row[1] );
            s.biz_code = str( row[2] );
            s.code = str( row[3] );
            s.name = str( row[4] );
            s.seq_no = toInt( row[5] );
            return s;
        }
        return null;
    }

    public static AppInfo queryAppInfo(String app_code) {
        AppInfo ai = new AppInfo();

        // current step is ?
        ai.curStep = getCurStep( app_code );
        if ( ai.curStep == null ) return ai;

        // how many steps does this application have ?
        String sql = "select s.id, s.tpl_id, s.biz_code, s.code, s.name, s.seq_no from wf_step s "//
                + " where s.biz_code = (select a.biz_code from wf_biz_app a where a.app_code =?)";
        for ( Object [] row : jdbc().list( sql, Arrays.asList( app_code ), 6 ) ) {
            Step s = new Step();
            s.id = toInt( row[0] );
            s.tpl_id = toInt( row[1] );
            s.biz_code = str( row[2] );
            s.code = str( row[3] );
            s.name = str( row[4] );
            s.seq_no = toInt( row[5] );
            ai.steps.add( s );
        }

        // render ctrls
        sql = "select id,tpl_id,name,type,extra_values,seq_no,style,require,regex,access_role,access_right "//
                + " from wf_tpl_ctrl where tpl_id = ( "//
                + " select tpl_id from wf_step where code = (select step_code from wf_biz_app where app_code=?)) order by seq_no asc";
        for ( Object [] row : jdbc().list( sql, Arrays.asList( app_code ), 11 ) ) {
            TplCtrl c = new TplCtrl();
            c.id = toInt( row[0] );
            c.tpl_id = toInt( row[1] );
            c.name = str( row[2] );
            c.type = str( row[3] );
            c.extra_values = str( row[4] );
            c.seq_no = toInt( row[5] );
            c.style = str( row[6] );
            c.require = toBool( str( row[7] ) );
            c.regex = str( row[8] );
            c.access_role = str( row[9] );
            c.access_right = str( row[10] );
            ai.ctrls.add( c );
        }
        return ai;
    }
}
