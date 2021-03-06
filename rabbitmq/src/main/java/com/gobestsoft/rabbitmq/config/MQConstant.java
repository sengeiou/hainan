package com.gobestsoft.rabbitmq.config;

/**
 * Created by gutongwei on 2018/2/7.
 *
 * @desc Rabbit消息队列相关常量
 */
public class MQConstant {

    private MQConstant() {
    }

    //exchange name
    public static final String DEFAULT_EXCHANGE = "hn.exchange";

    //DLX QUEUE
    public static final String DEFAULT_LETTER_SENDER_QUEUE_NAME = "hn.letter.sender";

    //DLX repeat QUEUE 私信转发队列
    public static final String DEFAULT_LETTER_RECEIVE_QUEUE_NAME = "hn.letter.receive";

    /**
     * 后台发送接口
     */
    public static final String ADMIN_GIVE_API_QUEUE = "hn.admin_give_api.queue";


    /**
     * 后台发送接口
     */
    public static final String API_GIBE_ADMIN = "hn.api_give_admin.queue";

    public static final String API_GIBE_ADMIN_EXCLUDE = "hn.api_give_admin_exclude.queue";

    /**
     * 会员导入接口
     */
    public static final String MEMBER_IMPORT_QUEUE = "hn.member_import.queue";
}
