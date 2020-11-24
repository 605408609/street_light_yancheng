/**
 * @filename:${entityName}Controller ${createTime}
 * @project ${project}  ${version}
 * Copyright(c) 2020 ${author} Co. Ltd.
 * All right reserved.
 */
package ${abstractControllerUrl};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ${coreUrl}.Result;
import ${coreUrl}.PageParam;
<#if isSwagger=="true" >
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
</#if>

/**
 * @Description:TODO(${entityComment}AbstractController)
 *
 * @version: ${version}
 * @author:  ${author}
 * @time     ${createTime}
 */
public class AbstractController<S extends IService<T>,T>{

	@Autowired
    protected S baseService;

	protected Result<T> result = new Result<T>();
	/**
	 * @explain 查询对象  <swagger GET请求>
	 * @author  ${author}
	 * @time    ${createTime}
	 */
	@GetMapping("/{id}")
	<#if isSwagger=="true" >
	@ApiOperation(value = "获取对象", notes = "作者：${author}")
	@ApiImplicitParam(paramType="path", name = "id", value = "对象id", required = true, dataType = "Long")
	</#if>
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
	 * @author  ${author}
	 * @time    ${createTime}
	 */
	@DeleteMapping("/{id}")
	<#if isSwagger=="true" >
	@ApiOperation(value = "删除", notes = "作者：${author}")
	@ApiImplicitParam(paramType="query", name = "id", value = "对象id", required = true, dataType = "Long")
	</#if>
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
	 * @author  ${author}
	 * @time    ${createTime}
	 */
	@PostMapping
	<#if isSwagger=="true" >
	@ApiOperation(value = "添加", notes = "作者：${author}")
	</#if>
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
	 * @author  ${author}
	 * @time    ${createTime}
	 */
	@PutMapping
	<#if isSwagger=="true" >
	@ApiOperation(value = "修改", notes = "作者：${author}")
	</#if>
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
	 * @author  ${author}
	 * @time    ${createTime}
	 */
	@GetMapping
	<#if isSwagger=="true" >
	@ApiOperation(value = "分页查询", notes = "分页查询,作者：${author}")
	</#if>
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
