package com.mapbox.flutter;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.view.TextureView;

import com.mapbox.mapboxsdk.maps.renderer.MapRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * The {@link SurfaceTextureMapRenderer} encapsulates the GL thread and
 * {@link TextureView} specifics to render the map.
 *
 * @see MapRenderer
 */
public class SurfaceTextureMapRenderer extends MapRenderer {
  private SurfaceTextureRenderThread renderThread;
  private boolean translucentSurface;

  /**
   * Create a {@link MapRenderer} for the given {@link TextureView}
   *
   * @param context                  the current Context
   * @param surfaceTexture              the SurfaceTexture
   * @param localIdeographFontFamily the local font family
   * @param translucentSurface    the translucency flag
   */
  public SurfaceTextureMapRenderer(@NonNull Context context,
                                   @NonNull SurfaceTexture surfaceTexture,
                                   int width,
                                   int height,
                                   String localIdeographFontFamily,
                                   boolean translucentSurface) {
    super(context, localIdeographFontFamily);
    this.translucentSurface = translucentSurface;
    renderThread = new SurfaceTextureRenderThread(surfaceTexture, width, height, this);
    renderThread.start();
  }

  /**
   * Overridden to provide package access
   */
  @Override
  protected void onSurfaceCreated(GL10 gl, EGLConfig config) {
    super.onSurfaceCreated(gl, config);
  }

  /**
   * Overridden to provide package access
   */
  @Override
  protected void onSurfaceChanged(GL10 gl, int width, int height) {
    super.onSurfaceChanged(gl, width, height);
  }

  /**
   * Overridden to provide package access
   */
  @Override
  protected void onDrawFrame(GL10 gl) {
    super.onDrawFrame(gl);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requestRender() {
    renderThread.requestRender();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void queueEvent(Runnable runnable) {
    renderThread.queueEvent(runnable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onStop() {
    renderThread.onPause();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onStart() {
    renderThread.onResume();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onDestroy() {
    renderThread.onDestroy();
  }

  public boolean isTranslucentSurface() {
    return translucentSurface;
  }
}
