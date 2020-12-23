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

/**
 * 线程同步缓存区
 *
 * 实现线程同步功能，与序列化缓存区共同保证二级缓存线程安全。若blocking=false关闭则SynchronizedCache位于责任链的最前端，
 * 否则就位于BlockingCache后面而BlockingCache位于责任链的最前端，从而保证了整条责任链是线程同步的。
 *
 * 源码分析：对于操作缓存的方法都进行了线程同步功能：synchronized
 *
 * @author Clinton Begin
 */
public class SynchronizedCache implements Cache {

  private final Cache delegate;

  public SynchronizedCache(Cache delegate) {
    this.delegate = delegate;
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  @Override
  public synchronized int getSize() {
    return delegate.getSize();
  }

  @Override
  public synchronized void putObject(Object key, Object object) {
    delegate.putObject(key, object);
  }

  @Override
  public synchronized Object getObject(Object key) {
    return delegate.getObject(key);
  }

  @Override
  public synchronized Object removeObject(Object key) {
    return delegate.removeObject(key);
  }

  @Override
  public synchronized void clear() {
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

}
