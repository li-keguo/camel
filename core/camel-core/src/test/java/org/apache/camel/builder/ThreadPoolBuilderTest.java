/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.builder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.spi.Registry;
import org.apache.camel.util.concurrent.ThreadPoolRejectedPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadPoolBuilderTest extends ContextTestSupport {

    @Override
    protected Registry createRegistry() throws Exception {
        Registry jndi = super.createRegistry();
        ExecutorService someone = Executors.newCachedThreadPool();
        jndi.bind("someonesPool", someone);
        return jndi;
    }

    @Test
    public void testThreadPoolBuilderDefault() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ExecutorService executor = builder.build(this, "myPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderMaxQueueSize() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ExecutorService executor = builder.maxQueueSize(2000).build(this, "myPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderMax() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ExecutorService executor = builder.maxPoolSize(100).build(this, "myPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderCoreAndMax() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ExecutorService executor = builder.poolSize(50).maxPoolSize(100).build(this, "myPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderKeepAlive() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ExecutorService executor = builder.keepAliveTime(30).build(this, "myPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderKeepAliveTimeUnit() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ExecutorService executor = builder.keepAliveTime(20000, TimeUnit.MILLISECONDS).build(this, "myPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderAll() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ExecutorService executor
                = builder.poolSize(50).maxPoolSize(100).maxQueueSize(2000).keepAliveTime(20000, TimeUnit.MILLISECONDS)
                        .rejectedPolicy(ThreadPoolRejectedPolicy.DiscardOldest).build(this, "myPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderTwoPoolsDefault() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ExecutorService executor = builder.build(this, "myPool");
        ExecutorService executor2 = builder.build(this, "myOtherPool");

        assertNotNull(executor);
        assertNotNull(executor2);

        assertFalse(executor.isShutdown());
        assertFalse(executor2.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
        assertTrue(executor2.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderScheduled() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ScheduledExecutorService executor = builder.poolSize(5).maxQueueSize(2000).buildScheduled();
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderScheduledName() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ScheduledExecutorService executor = builder.poolSize(5).maxQueueSize(2000).buildScheduled("myScheduledPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

    @Test
    public void testThreadPoolBuilderScheduledSourceName() throws Exception {
        ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
        ScheduledExecutorService executor = builder.poolSize(5).maxQueueSize(2000).buildScheduled(this, "myScheduledPool");
        assertNotNull(executor);

        assertFalse(executor.isShutdown());
        context.stop();
        assertTrue(executor.isShutdown());
    }

}
