package com.exc.street.light.electricity.task;

import com.exc.street.light.electricity.service.CanSceneService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.Scene;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

/**
 * 场景定时任务
 *
 * @author LinShiWen
 * @date 2017/12/8
 */
@Setter
@Getter
public class SceneTask implements Callable<Result> {
    private Scene scene;
    private CanSceneService service;
    private HttpServletRequest request;

    public SceneTask(Scene scene, CanSceneService service, HttpServletRequest request) {
        this.scene = scene;
        this.service = service;
        this.request = request;
    }

    @Override
    public Result call() throws Exception {
        return service.add(1,scene, request);
    }
}
