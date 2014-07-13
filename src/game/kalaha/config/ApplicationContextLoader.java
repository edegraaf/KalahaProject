package game.kalaha.config;

import java.net.URISyntaxException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *	Singleton to load the context containing the Spring beans<br/><br/>
 *
 *  Copyright (C) 2014  Edgar H. de Graaf
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Copyright (C) 2014  Edgar H. de Graaf, edgargithub&#64;outlook.com
 *  
 *  @author Edgar de Graaf
 */
public class ApplicationContextLoader {

	private static ApplicationContextLoader instance = null;

	private ApplicationContext appContext;

	private ApplicationContextLoader() throws URISyntaxException {
		try {
			this.appContext = new ClassPathXmlApplicationContext("beanContext.xml");
		} catch (Exception e) {
			this.appContext = new FileSystemXmlApplicationContext("beanContext.xml");
		}
	}

	public static ApplicationContextLoader getInstance() {
		if (instance == null) {
			try {
				instance = new ApplicationContextLoader();
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return null;
			}
		}
		return instance;
	}

	public ApplicationContext getAppContext() {
		return this.appContext;
	}
}
