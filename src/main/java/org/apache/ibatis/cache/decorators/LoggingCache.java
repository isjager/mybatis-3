/**
 *    Copyright 2009-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/**
 * 统计二级缓存命中率并输出打印，日志中出现"Cache Hit Ratio"便表示命中了二级缓存
 *
 * @author Clinton Begin
 */
public class LoggingCache implements Cache {

  private final Log log;
  private final Cache delegate;
  protected int requests = 0;
  protected int hits = 0;

  public LoggingCache(Cache delegate) {
    this.delegate = delegate;
    this.log = LogFactory.getLog(getId());
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  @Override
  public int getSize() {
    return delegate.getSize();
  }

  @Override
  public void putObject(Object key, Object object) {
    delegate.putObject(key, object);
  }

  @Override
  public Object getObject(Object key) {
    // 执行一次查询加一次
    requests++;
    // 查询缓存中是否已经存在
    final Object value = delegate.getObject(key);
    if (value != null) {
      // 命中一次加一次
      hits++;
    }
    // 开启debug日志
    if (log.isDebugEnabled()) {
      log.debug("Cache Hit Ratio [" + getId() + "]: " + getHitRatio());
    }
    return value;
  }

  @Override
  public Object removeObject(Object key) {
    return delegate.removeObject(key);
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return delegate.equals(obj);
  }

  /**
   * 计算命中率
   */
  private double getHitRatio() {
    // 命中次数：查询次数
    return (double) hits / (double) requests;
  }

}
