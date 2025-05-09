package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.Download;
import org.example.mapper.DownloadMapper;
import org.example.service.IDownloadService;
import org.springframework.stereotype.Service;

@Service
public class DownloadServiceImpl extends ServiceImpl<DownloadMapper, Download> implements IDownloadService {
}
