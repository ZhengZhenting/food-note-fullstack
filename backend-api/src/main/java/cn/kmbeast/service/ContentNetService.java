package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.vo.ContentNetVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * contentNet interface
 */

public interface ContentNetService {

    Result<String> save(ContentNet contentNet);

    Result<String> update(ContentNet contentNet);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<ContentNetVO>> query(ContentNetQueryDto contentNetQueryDto);

    Result<Object> findContent(ContentNetQueryDto contentNetQueryDto);

    Result<Boolean> authStatus(ContentNetQueryDto contentNetQueryDto);
}
