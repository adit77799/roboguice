/*
 * Copyright 2009 Michael Burton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package roboguice.inject;

import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import roboguice.inject.delayedInjection.FieldMemberInjectorFactory;

import java.lang.reflect.Field;

/**
 * 
 * @author Mike Burton
 * @author Pierre-Yves Ricau (py.ricau+roboguice@gmail.com)
 */
public class ExtrasListenerFactory implements FieldMemberInjectorFactory<InjectExtra> {

    protected Provider<Context> contextProvider;

    public ExtrasListenerFactory(Provider<Context> contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public <T> MembersInjector<T> buildFieldMemberInjector(Class<?> clazz, Field field, InjectExtra annotation) {
        return new ExtrasMembersInjector<T>(field, contextProvider, annotation);
    }
}
