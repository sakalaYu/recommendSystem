package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.Browse;
import org.example.mapper.BrowseMapper;
import org.example.service.IBrowseService;
import org.springframework.stereotype.Service;

@Service
public class BrowseServiceImpl extends ServiceImpl<BrowseMapper, Browse> implements IBrowseService {
}