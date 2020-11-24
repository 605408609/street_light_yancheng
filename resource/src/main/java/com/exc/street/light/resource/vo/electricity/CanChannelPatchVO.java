package com.exc.street.light.resource.vo.electricity;

import com.exc.street.light.resource.entity.electricity.CanChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * can回路批量编辑
 *
 * @author Linshiwen
 * @date 2018/5/26
 */
@Setter
@Getter
@ToString
public class CanChannelPatchVO {
    /**
     * 回路集合
     */
    private List<CanChannel> canChannels;
}
