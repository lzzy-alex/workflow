package com.ucap.workflow.entity;

import static com.ucap.toolkit.type.StringUtil.str;

import com.ucap.toolkit.file.ImageUtil;
import com.ucap.toolkit.type.StringUtil;

public enum CtrlType {

    text("纯文本"), href("超链接"), input("文本框"), textarea("文本域"), radio("单选项"), checkbox("复选项"), file("文件"), date("日期"), btn_forward("下一步"), btn_backward("上一步"), btn_def_step("自定义步骤"), btn_ret_mod(
            "退回企业修改"), btn_stop("中断结束"), btn_saveonly("暂存"), btn_dev("编程式控件");

    public static boolean isButton(TplCtrl c) {
        String type = c.type;
        return type.equals( CtrlType.btn_forward.val() ) || type.equals( CtrlType.btn_backward.val() ) || type.equals( CtrlType.btn_def_step.val() ) || type.equals( CtrlType.btn_stop
                .val() ) || type.equals( CtrlType.btn_saveonly.val() ) || type.equals( CtrlType.btn_dev.val() ) || type.equals( CtrlType.btn_ret_mod.val() );
    }

    public static boolean isHref(TplCtrl c) {
        return c.type.equals( CtrlType.href.val() );
    }

    public static String toHtml(TplCtrl c) {
        String cid = str( c.id ); // control id
        String t = c.type;
        String lb = str( c.name, "" ); // label name
        String cls = str( c.style, "" );
        String ev = str( c.extra_values, "" );
        String req = str( c.require, "false" );
        String regex = str( c.regex, "" );
        String cd = !StringUtil.isEmpty( c.ctrl_dat ) ? str( c.ctrl_dat.dat, "" ) : "";
        String readonly = c.readonly ? " readonly='readonly' " : "";
        String readonlyIcon = c.readonly ? "<img src='/ujet/images/question.png' title='该控件由外部用户填写' style='width:20px'/>" : "";

        String fmt = null;
        if ( t.equals( text.val() ) ) return ev;// plain text

        if ( t.equals( href.val() ) ) {
            fmt = "<a name='%s' href='%s?app_id=%s' class='%s'>%s</a>";
            return String.format( fmt, cid, ev, c.app_code, cls, lb );
        }

        if ( t.equals( input.val() ) ) {
            fmt = "<input name='%s' type='text' value='%s' class='%s' require='%s' regex='%s' %s/> %s";
            return String.format( fmt, cid, cd, cls, req, regex, readonly, readonlyIcon );
        }

        if ( t.equals( textarea.val() ) ) {
            fmt = "<textarea name='%s' cols='45' rows='5' class='%s' require='%s' regex='%s' %s>%s</textarea> %s";
            return String.format( fmt, cid, cls, req, regex, readonly, cd, readonlyIcon );
        }

        if ( t.equals( radio.val() ) ) {
            readonly = c.readonly ? " disabled " : "";
            StringBuffer buf = new StringBuffer();
            for ( String v : ev.split( "_" ) ) {
                String ck = v.equals( cd ) ? "checked" : "";
                fmt = "<input name='%s' type='radio' value='%s' %s class='%s' require='%s' %s/> %s&nbsp;&nbsp;&nbsp;";
                buf.append( String.format( fmt, cid, v, ck, cls, req, readonly, v ) );
            }
            return buf.toString() + readonlyIcon;
        }

        if ( t.equals( checkbox.val() ) ) {
            readonly = c.readonly ? " disabled " : "";
            StringBuffer buf = new StringBuffer();
            String [] opts = ev.split( "_" ); // options
            String [] ckopts = cd.split( "_" ); // checked options
            for ( String opt : opts ) {
                String ck = "";
                for ( String ckopt : ckopts ) {
                    if ( !ckopt.equals( opt ) ) continue;
                    ck = "checked";
                    break;
                }
                fmt = "<input name='%s' type='checkbox' value='%s' %s class='%s' require='%s' %s/> %s&nbsp;&nbsp;&nbsp;";
                buf.append( String.format( fmt, cid, opt, ck, cls, req, readonly, opt ) );
            }
            return buf.toString() + readonlyIcon;
        }

        if ( t.equals( file.val() ) ) {
            readonly = c.readonly ? " disabled " : "";
            String fileLink = "";
            if ( !StringUtil.isEmpty( cd ) ) {
                int pos = cd.lastIndexOf( "_" );
                String fileName = cd.substring( 0, pos );
                String realName = cd.substring( pos + 1, cd.length() );
                String url = "upload/audit/" + fileName;
                if ( !ImageUtil.isImage( fileName ) ) url = "downloadFileProcedure.action?path=jet/project/" + url + "&fname=" + fileName;
                fileLink = String.format( "<a href='%s' id='file_%s' target='_blank'>%s</a>", url, cid, realName );
            }
            fmt = "%s<br/><input id='%s' name='files' type='file' class='%s' require='%s' regex='%s' %s/> %s";
            return String.format( fmt, fileLink, cid, cls, req, regex, readonly, readonlyIcon );
        }

        if ( t.equals( date.val() ) ) {
            fmt = "<input name='%s' type='text' value='%s' class='inputxt %s' require='%s' regex='%s' placeholder='2016-10-01' %s autocomplete='off' onClick=\"WdatePicker({startDate:'%%y/%%M/01 00:00:00',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})\"/> %s";
            return String.format( fmt, cid, cd, cls, req, regex, readonly, readonlyIcon );
        }

        if ( t.equals( btn_forward.val() ) || t.equals( btn_backward.val() ) || t.equals( btn_def_step.val() ) || t.equals( btn_stop.val() ) || t.equals( btn_saveonly.val() ) || t.equals( btn_ret_mod
                .val() ) ) {
            readonly = c.readonly ? " disabled " : "";
            fmt = "<input name='%s' type='submit' value='%s' class='%s' %s/>";
            return String.format( fmt, cid, lb, cls, readonly );
        }

        if ( t.equals( btn_dev.val() ) ) {
            readonly = c.readonly ? " disabled " : "";
            fmt = "<input type='button' value='%s' onclick='javascript:window.open(\"%s?procedure_id=%s&app_id=%s\", %s)' class='%s' %s/>";
            String style = "\"_blank\",\"toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=800, height=600\"";
            return String.format( fmt, lb, ev, c.biz_code, c.app_code, style, cls, readonly );
        }

        // should not happen
        return null;
    }

    private String name;

    private CtrlType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String val() {
        return this.toString();
    }
}
