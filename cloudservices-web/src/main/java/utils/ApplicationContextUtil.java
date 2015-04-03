/************************************************************************ 
版权所有 (C)2010, 深圳市康佳集团股份有限公司。
*                    
* 文件名称：ApplicationContextUtil.java               
* 文件标识：          
* 内容摘要：Spring上下文
* 其它说明： 
* 当前版本：1.0              
* 作 者： 邱剑锋         
* 完成日期：2012-6-28              
* 修改记录： 
* 修改日期：                 
* 版 本 号：                 
* 修 改 人：                 
* 修改内容：                 
************************************************************************/
package utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/** 
 * 类说明：  Spring上下文
 * @author 邱剑锋
 * @date    2012-6-28
 * @version 1.0
 */ 
public class ApplicationContextUtil implements ApplicationContextAware 
{
	private static ApplicationContext context;
	public void setApplicationContext(ApplicationContext contex) throws BeansException 
	{
		this.context= contex; 

	}
	public static Object getBeanByName(String beanName)
	{
		return context.getBean(beanName);
	}

}
