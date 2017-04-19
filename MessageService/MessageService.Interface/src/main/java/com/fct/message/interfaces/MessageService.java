package com.fct.message.interfaces;

/**
 * Created by jon on 2017/4/11.
 */
public interface MessageService {

    /// <summary>
    /// 保存消息
    /// </summary>
//    void create(MessageQueue message);

    /// <summary>
    /// 更新消息置位
    /// </summary>
    void success(Long id);

    /// <summary>
    /// 请求处理失败
    /// </summary>
    /// <returns></returns>
    void fail(Long id,String message);

    /// <summary>
    /// 恢复消息
    /// </summary>
    /// <returns></returns>
    void resume(Long id);

    /// <summary>
    /// 根据模块获取消息体
    /// </summary>
//    List<MessageQueue> find(String typeId);

    /// <summary>
    /// 获取异常消息
    /// </summary>
    /// <returns></returns>
//    List<MessageQueue> findAll(String targetModule, Integer pageIndex, Integer pageSize);
}
