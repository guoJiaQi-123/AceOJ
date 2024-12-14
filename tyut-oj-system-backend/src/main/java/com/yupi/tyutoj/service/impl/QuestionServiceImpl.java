package com.yupi.tyutoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.tyutoj.mapper.QuestionMapper;
import com.yupi.tyutoj.model.entity.Question;
import com.yupi.tyutoj.service.QuestionService;
import org.springframework.stereotype.Service;

/**
 * @author HX
 * @description 针对表【question(帖子)】的数据库操作Service实现
 * @createDate 2024-12-14 17:06:34
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

}




