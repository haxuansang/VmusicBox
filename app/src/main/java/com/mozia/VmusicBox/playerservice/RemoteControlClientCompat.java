package com.mozia.VmusicBox.playerservice;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.Method;

public class RemoteControlClientCompat {
    private static final String TAG = RemoteControlClientCompat.class.getSimpleName();
    private static boolean sHasRemoteControlAPIs;
    private static Method sRCCEditMetadataMethod;
    private static Method sRCCSetPlayStateMethod;
    private static Method sRCCSetTransportControlFlags;
    private static Class sRemoteControlClientClass;
    private Object mActualRemoteControlClient;

    public class MetadataEditorCompat {
        public static final int METADATA_KEY_ARTWORK = 100;
        private Object mActualMetadataEditor;
        private Method mApplyMethod;
        private Method mClearMethod;
        private Method mPutBitmapMethod;
        private Method mPutLongMethod;
        private Method mPutStringMethod;

        private MetadataEditorCompat(Object actualMetadataEditor) {
            if (RemoteControlClientCompat.sHasRemoteControlAPIs && actualMetadataEditor == null) {
                throw new IllegalArgumentException("Remote Control API's exist, should not be given a null MetadataEditor");
            }
            if (RemoteControlClientCompat.sHasRemoteControlAPIs) {
                Class metadataEditorClass = actualMetadataEditor.getClass();
                try {
                    this.mPutStringMethod = metadataEditorClass.getMethod("putString", new Class[]{Integer.TYPE, String.class});
                    this.mPutBitmapMethod = metadataEditorClass.getMethod("putBitmap", new Class[]{Integer.TYPE, Bitmap.class});
                    this.mPutLongMethod = metadataEditorClass.getMethod("putLong", new Class[]{Integer.TYPE, Long.TYPE});
                    this.mClearMethod = metadataEditorClass.getMethod("clear", new Class[0]);
                    this.mApplyMethod = metadataEditorClass.getMethod("apply", new Class[0]);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            this.mActualMetadataEditor = actualMetadataEditor;
        }

        public MetadataEditorCompat putString(int key, String value) {
            if (RemoteControlClientCompat.sHasRemoteControlAPIs) {
                try {
                    this.mPutStringMethod.invoke(this.mActualMetadataEditor, new Object[]{Integer.valueOf(key), value});
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            return this;
        }

        public MetadataEditorCompat putBitmap(int key, Bitmap bitmap) {
            if (RemoteControlClientCompat.sHasRemoteControlAPIs) {
                try {
                    this.mPutBitmapMethod.invoke(this.mActualMetadataEditor, new Object[]{Integer.valueOf(key), bitmap});
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            return this;
        }

        public MetadataEditorCompat putLong(int key, long value) {
            if (RemoteControlClientCompat.sHasRemoteControlAPIs) {
                try {
                    this.mPutLongMethod.invoke(this.mActualMetadataEditor, new Object[]{Integer.valueOf(key), Long.valueOf(value)});
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            return this;
        }

        public void clear() {
            if (RemoteControlClientCompat.sHasRemoteControlAPIs) {
                try {
                    this.mClearMethod.invoke(this.mActualMetadataEditor, null);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }

        public void apply() {
            if (RemoteControlClientCompat.sHasRemoteControlAPIs) {
                try {
                    this.mApplyMethod.invoke(this.mActualMetadataEditor, null);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }
//TODO check why
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
//    static {
//        /*
//        r5 = 0;
//        r6 = com.mozia.Mp3OnlineFree.playerservice.RemoteControlClientCompat.class;
//        r6 = r6.getSimpleName();
//        TAG = r6;
//        sHasRemoteControlAPIs = r5;
//        r6 = com.mozia.Mp3OnlineFree.playerservice.RemoteControlClientCompat.class;
//        r0 = r6.getClassLoader();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r6 = getActualRemoteControlClientClass(r0);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        sRemoteControlClientClass = r6;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r6 = com.mozia.Mp3OnlineFree.playerservice.RemoteControlClientCompat.class;
//        r6 = r6.getFields();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r7 = r6.length;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//    L_0x001e:
//        if (r5 < r7) goto L_0x005a;
//    L_0x0020:
//        r5 = sRemoteControlClientClass;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r6 = "editMetadata";
//        r7 = 1;
//        r7 = new java.lang.Class[r7];	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r8 = 0;
//        r9 = java.lang.Boolean.TYPE;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r7[r8] = r9;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r5 = r5.getMethod(r6, r7);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        sRCCEditMetadataMethod = r5;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r5 = sRemoteControlClientClass;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r6 = "setPlaybackState";
//        r7 = 1;
//        r7 = new java.lang.Class[r7];	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r8 = 0;
//        r9 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r7[r8] = r9;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r5 = r5.getMethod(r6, r7);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        sRCCSetPlayStateMethod = r5;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r5 = sRemoteControlClientClass;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r6 = "setTransportControlFlags";
//        r7 = 1;
//        r7 = new java.lang.Class[r7];	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r8 = 0;
//        r9 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r7[r8] = r9;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r5 = r5.getMethod(r6, r7);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        sRCCSetTransportControlFlags = r5;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r5 = 1;
//        sHasRemoteControlAPIs = r5;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//    L_0x0059:
//        return;
//    L_0x005a:
//        r2 = r6[r5];	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r8 = sRemoteControlClientClass;	 Catch:{ NoSuchFieldException -> 0x0072, IllegalArgumentException -> 0x0091, IllegalAccessException -> 0x00be, ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, SecurityException -> 0x00ec }
//        r9 = r2.getName();	 Catch:{ NoSuchFieldException -> 0x0072, IllegalArgumentException -> 0x0091, IllegalAccessException -> 0x00be, ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, SecurityException -> 0x00ec }
//        r3 = r8.getField(r9);	 Catch:{ NoSuchFieldException -> 0x0072, IllegalArgumentException -> 0x0091, IllegalAccessException -> 0x00be, ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, SecurityException -> 0x00ec }
//        r8 = 0;
//        r4 = r3.get(r8);	 Catch:{ NoSuchFieldException -> 0x0072, IllegalArgumentException -> 0x0091, IllegalAccessException -> 0x00be, ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, SecurityException -> 0x00ec }
//        r8 = 0;
//        r2.set(r8, r4);	 Catch:{ NoSuchFieldException -> 0x0072, IllegalArgumentException -> 0x0091, IllegalAccessException -> 0x00be, ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, SecurityException -> 0x00ec }
//    L_0x006f:
//        r5 = r5 + 1;
//        goto L_0x001e;
//    L_0x0072:
//        r1 = move-exception;
//        r8 = TAG;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = "Could not get real field: ";
//        r9.<init>(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = r2.getName();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = r9.toString();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        com.ypyproductions.utils.DBLog.m26e(r8, r9);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        goto L_0x006f;
//    L_0x008c:
//        r1 = move-exception;
//        r1.printStackTrace();
//        goto L_0x0059;
//    L_0x0091:
//        r1 = move-exception;
//        r8 = TAG;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = "Error trying to pull field value for: ";
//        r9.<init>(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = r2.getName();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = " ";
//        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = r1.getMessage();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = r9.toString();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        com.ypyproductions.utils.DBLog.m26e(r8, r9);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        goto L_0x006f;
//    L_0x00b9:
//        r1 = move-exception;
//        r1.printStackTrace();
//        goto L_0x0059;
//    L_0x00be:
//        r1 = move-exception;
//        r8 = TAG;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = "Error trying to pull field value for: ";
//        r9.<init>(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = r2.getName();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = " ";
//        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r10 = r1.getMessage();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = r9.append(r10);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        r9 = r9.toString();	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        com.ypyproductions.utils.DBLog.m26e(r8, r9);	 Catch:{ ClassNotFoundException -> 0x008c, NoSuchMethodException -> 0x00b9, IllegalArgumentException -> 0x00e6, SecurityException -> 0x00ec }
//        goto L_0x006f;
//    L_0x00e6:
//        r1 = move-exception;
//        r1.printStackTrace();
//        goto L_0x0059;
//    L_0x00ec:
//        r1 = move-exception;
//        r1.printStackTrace();
//        goto L_0x0059;
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mozia.Mp3OnlineFree.playerservice.RemoteControlClientCompat.<clinit>():void");
//    }

    public static Class getActualRemoteControlClientClass(ClassLoader classLoader) throws ClassNotFoundException {
        return classLoader.loadClass("android.media.RemoteControlClient");
    }

    public RemoteControlClientCompat(PendingIntent pendingIntent) {
        if (sHasRemoteControlAPIs) {
            try {
                this.mActualRemoteControlClient = sRemoteControlClientClass.getConstructor(new Class[]{PendingIntent.class}).newInstance(new Object[]{pendingIntent});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public RemoteControlClientCompat(PendingIntent pendingIntent, Looper looper) {
        if (sHasRemoteControlAPIs) {
            try {
                this.mActualRemoteControlClient = sRemoteControlClientClass.getConstructor(new Class[]{PendingIntent.class, Looper.class}).newInstance(new Object[]{pendingIntent, looper});
            } catch (Exception e) {
                Log.e(TAG, "Error creating new instance of " + sRemoteControlClientClass.getName(), e);
            }
        }
    }

    public MetadataEditorCompat editMetadata(boolean startEmpty) {
        Object invoke;
        if (sHasRemoteControlAPIs) {
            try {
                invoke = sRCCEditMetadataMethod.invoke(this.mActualRemoteControlClient, new Object[]{Boolean.valueOf(startEmpty)});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        invoke = null;
        return new MetadataEditorCompat(invoke);
    }

    public void setPlaybackState(int state) {
        if (sHasRemoteControlAPIs) {
            try {
                sRCCSetPlayStateMethod.invoke(this.mActualRemoteControlClient, new Object[]{Integer.valueOf(state)});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setTransportControlFlags(int transportControlFlags) {
        if (sHasRemoteControlAPIs) {
            try {
                sRCCSetTransportControlFlags.invoke(this.mActualRemoteControlClient, new Object[]{Integer.valueOf(transportControlFlags)});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final Object getActualRemoteControlClientObject() {
        return this.mActualRemoteControlClient;
    }
}
