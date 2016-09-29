package com.ucap.workflow.util;

import com.ucap.toolkit.file.PropertyUtil;
import com.ucap.toolkit.jdbc.JDBC;

public class Toolkit {

    // config reader
    private static PropertyUtil config = new PropertyUtil( "/wf_config.properties" );

    // jdbc
    private static final String driver = config().get( "db_driver" );
    private static final String url = config().get( "db_url" );
    private static final String uname = config().get( "db_uname" );
    private static final String pwd = config().get( "db_pwd" );
    private static JDBC jdbc = new JDBC( driver, url, uname, pwd );

    /** property file util */
    public static PropertyUtil config() {
        return config;
    }

    public static JDBC jdbc() {
        return jdbc;
    }

}
