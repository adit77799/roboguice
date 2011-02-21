package roboguice.inject;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import roboguice.inject.delayedInjection.MemberInjectorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class CustomInjectionRegistrationListener implements TypeListener {

    protected Map<Class<? extends Annotation>, MemberInjectorFactory> factoryMap = new HashMap<Class<? extends Annotation>, MemberInjectorFactory>();

    public void registerMemberInjector(Class<? extends Annotation> annotation, MemberInjectorFactory factory){
        if(annotation != null && factory != null){
            factoryMap.put(annotation, factory);
        }
    }

    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        scanClassForAnnotations(typeEncounter, typeLiteral.getRawType());
    }

    private <I> void scanClassForAnnotations(TypeEncounter<I> typeEncounter, Class<?> inputClazz) {
        for( Class<?> clazz = inputClazz; clazz!=Object.class ; clazz = clazz.getSuperclass() ) {
            for (Field field : clazz.getDeclaredFields()) {
                registerFieldMemberInjector(typeEncounter, clazz, field);
            }

            for (Method method : clazz.getDeclaredMethods()) {
                registerMethodMemberInjector(typeEncounter, clazz, method);
                scanMethodParametersForAnnotations(typeEncounter, clazz, method);
            }

            for( Class<?> inputInterfaceClazz : clazz.getInterfaces()){
                for (Method method : inputInterfaceClazz.getMethods()) {
                    registerMethodMemberInjector(typeEncounter, inputInterfaceClazz, method);
                    scanMethodParametersForAnnotations(typeEncounter, inputInterfaceClazz, method);
                }
            }
        }
    }

    private <I> void scanMethodParametersForAnnotations(TypeEncounter<I> typeEncounter, Class<?> clazz, Method method){
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int i = 0; i < parameterAnnotations.length; i++){
            final Annotation[] annotationArray = parameterAnnotations[i];
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Class<?> parameterType = parameterTypes[i];

            registerParameterMemberInjector(typeEncounter, clazz, method, annotationArray, parameterType);
        }
    }

    private <I> void registerParameterMemberInjector(TypeEncounter<I> typeEncounter, Class<?> clazz, Method method, Annotation[] annotationArray, Class<?> parameterType) {
       for(Annotation annotation : annotationArray){
           if(factoryMap.containsKey(annotation.annotationType())){
               factoryMap.get(annotation.annotationType()).registerParameter(typeEncounter, clazz, method, annotation, parameterType);
           }
       }
    }

    private <I> void registerMethodMemberInjector(TypeEncounter<I> typeEncounter, Class<?> clazz, Method method) {
        for(Annotation annotation : method.getDeclaredAnnotations()){
            if(factoryMap.containsKey(annotation.annotationType())){
                factoryMap.get(annotation.annotationType()).registerMethod(typeEncounter, clazz, method, annotation);
            }
        }
    }

    private <I> void registerFieldMemberInjector(TypeEncounter<I> typeEncounter, Class<?> clazz, Field field) {
        for(Annotation annotation : field.getDeclaredAnnotations()){
            if(factoryMap.containsKey(annotation.annotationType())){
                factoryMap.get(annotation.annotationType()).registerField(typeEncounter, clazz, field, annotation);
            }
        }
    }
}