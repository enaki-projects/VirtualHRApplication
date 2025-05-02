export interface VjsPlayerSettings {
  fluid: boolean;
  autoplay: boolean;
  controls: boolean;
  muted: boolean;
  sources: {
    src: string,
    type: string,
  }[];
}
