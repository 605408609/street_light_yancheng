/**
 * @filename:LogNormalController 2020-05-08
 * @project log  V1.0
 * Copyright(c) 2020 xuJiaHao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.log_biz.aid;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @Description:TODO(AbstractController)
 *
 * @version: V1.0
 * @author:  xuJiaHao
 * @time     2020-05-08
 */
public class AbstractController<S extends IService<T>,T>{

	@Autowired
    protected S baseService;

	protected Result<T> result = new Result<T>();
	/**
	 * @explain 查询对象  <swagger GET请求>
	 * @author  xuJiaHao
	 * @time    2020-05-08
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "获取对象", notes = "作者：xuJiaHao")
	@ApiImplicitParam(paramType="path", name = "id", value = "对象id", required = true, dataType = "Long")
	public Result<T> getById(@PathVariable("id") Long id){
		T obj=baseService.getById(id);
		if (null!=obj ) {
			 result.success(obj);
		}else {
			 result.error("查询对象不存在！");
		}
		return result;
	}

	/**
	 * @explain 删除对象
	 * @author  xuJiaHao
	 * @time    2020-05-08
	 */
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除", notes = "作者：xuJiaHao")
	@ApiImplicitParam(paramType="query", name = "id", value = "对象id", required = true, dataType = "Long")
	public Result<T> deleteById(@PathVariable("id") Long id){
		Result<T> result=new Result<T>();
		T obj=baseService.getById(id);
		if (null!=obj) {
		  boolean rsg = baseService.removeById(id);
		  if (rsg) {
			    result.success("删除成功");
		  }else {
			   result.error("删除失败！");
		  }
		}else {
			 result.error("删除的对象不存在！");
		}
		return result;
	}

	/**
	 * @explain 添加
	 * @author  xuJiaHao
	 * @time    2020-05-08
	 */
	@PostMapping
	@ApiOperation(value = "添加", notes = "作者：xuJiaHao")
	public Result<T> insert(@ApiParam @RequestBody T entity){
		Result<T> result=new Result<T>();
		if (null!=entity) {
			boolean rsg = baseService.save(entity);
			if (rsg) {
				  result.success("添加成功");
			  }else {
				  result.error("添加失败！");
			  }
		}else {
			result.error("请传入正确参数！");
		}
		return result;
	}

	/**
	 * @explain 修改
	 * @author  xuJiaHao
	 * @time    2020-05-08
	 */
	@PutMapping
	@ApiOperation(value = "修改", notes = "作者：xuJiaHao")
	public Result<T> update(@ApiParam @RequestBody T entity){
		Result<T> result=new Result<T>();
		if (null!=entity) {
			boolean rsg = baseService.updateById(entity);
			if (rsg) {
				  result.success("修改成功");
			  }else {
				  result.error("修改失败！");
			  }
		}else {
			result.error("请传入正确参数！");
		}
		return result;
	}

	/**
	 * @explain 分页条件查询
	 * @author  xuJiaHao
	 * @time    2020-05-08
	 */
	@GetMapping
	@ApiOperation(value = "分页查询", notes = "分页查询,作者：xuJiaHao")
	public Result<IPage<T>> getPages(PageParam<T> param, T t){
		Result<IPage<T>> returnPage=new Result<IPage<T>>();
		Page<T> page=new Page<T>(param.getPageNum(),param.getPageSize());
		QueryWrapper<T> queryWrapper =new QueryWrapper<T>();
		queryWrapper.setEntity(t);
		//分页数据
		IPage<T> pageData=baseService.page(page, queryWrapper);
		returnPage.success(pageData);

		return returnPage;
	}
}
