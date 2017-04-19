package com.fct.message.service;

import com.fct.message.interfaces.MessageService;
import org.springframework.stereotype.Service;

//import java.util.List;

/**
 * Created by jon on 2017/4/11.
 */
@Service(value = "messageService")
public class MessageServiceImpl implements MessageService {

//    @Override
//    public void create(MessageQueue message) {
//
//    }

    @Override
    public void success(Long id) {

    }

    @Override
    public void fail(Long id, String message) {

    }

    @Override
    public void resume(Long id) {

    }

//    @Override
//    public List<MessageQueue> find(String typeId) {
//        return null;
//    }
//
//    @Override
//    public List<MessageQueue> findAll(String targetModule, Integer pageIndex, Integer pageSize) {
//        return null;
//    }
}
