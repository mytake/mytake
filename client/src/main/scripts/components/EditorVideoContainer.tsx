import * as React from "react";
import * as keycode from "keycode";
import YouTube from "react-youtube";
import Video from "./Video";
import { fetchFact } from "../utils/databaseAPI";
import { Foundation } from "../java2ts/Foundation";
import { Routes } from "../java2ts/Routes";
import { VideoBlock } from "../java2ts/VideoBlock";
import { isWriteOnly, WritingEventHandlers } from "./BlockEditor";
import { alertErr } from "../utils/functions";

export interface EditorVideoContainerProps {
  idx: number;
  active: boolean;
  block: VideoBlock;
  eventHandlers?: WritingEventHandlers;
}

export interface EditorVideoContainerState {
  loading: boolean;
  videoFact?: Foundation.VideoFactContentFast;
}

class EditorVideoContainer extends React.Component<
  EditorVideoContainerProps,
  EditorVideoContainerState
> {
  constructor(props: EditorVideoContainerProps) {
    super(props);
    this.state = {
      loading: true
    };
  }
  getFact = (factHash: string) => {
    fetchFact(
      factHash,
      (
        error: string | Error | null,
        factContent: Foundation.VideoFactContentFast
      ) => {
        if (error) {
          if (typeof error != "string") {
            alertErr("EditorVideoContainer: " + error.message);
          } else {
            alertErr("EditorVideoContainer: " + error);
          }
          throw error;
        } else {
          this.setState({
            loading: false,
            videoFact: factContent
          });
        }
      }
    );
  };
  componentDidMount() {
    this.getFact(this.props.block.videoId);
  }
  componentWillReceiveProps(nextProps: EditorVideoContainerProps) {
    if (this.props.block.videoId !== nextProps.block.videoId) {
      this.getFact(nextProps.block.videoId);
    }
  }
  render() {
    return (
      <EditorVideoBranch
        containerProps={this.props}
        containerState={this.state}
      />
    );
  }
}

interface EditorVideoBranchProps {
  containerProps: EditorVideoContainerProps;
  containerState: EditorVideoContainerState;
}

export const EditorVideoBranch: React.StatelessComponent<
  EditorVideoBranchProps
> = props => {
  if (props.containerState.loading || !props.containerState.videoFact) {
    return <VideoLoadingView />;
  } else {
    return (
      <EditorVideo
        idx={props.containerProps.idx}
        active={props.containerProps.active}
        videoFact={props.containerState.videoFact}
        factHash={props.containerProps.block.videoId}
        range={props.containerProps.block.range}
        eventHandlers={props.containerProps.eventHandlers}
      />
    );
  }
};

const VideoLoadingView: React.StatelessComponent<{}> = props => (
  <div className="editor__document editor__document--base editor__document--hover">
    <h2 className="editor__document-title">Loading</h2>
  </div>
);

interface EditorVideoBlockProps {
  idx: number;
  active: boolean;
  videoFact: Foundation.VideoFactContentFast;
  factHash: string;
  range?: [number, number];
  eventHandlers?: WritingEventHandlers;
}
interface EditorVideoBlockState {}

class EditorVideo extends React.Component<
  EditorVideoBlockProps,
  EditorVideoBlockState
> {
  constructor(props: EditorVideoBlockProps) {
    super(props);
  }
  handleClick = () => {
    if (isWriteOnly(this.props.eventHandlers)) {
      this.props.eventHandlers.handleFocus(this.props.idx);
    }
  };
  handleFocus = () => {
    if (isWriteOnly(this.props.eventHandlers)) {
      this.props.eventHandlers.handleFocus(this.props.idx);
    }
  };
  handleKeyDown = (ev: React.KeyboardEvent<HTMLDivElement>) => {
    switch (ev.keyCode) {
      case keycode("enter"):
        if (isWriteOnly(this.props.eventHandlers)) {
          this.props.eventHandlers.handleEnterPress();
        }
        break;
      case keycode("backspace") || keycode("delete"):
        if (isWriteOnly(this.props.eventHandlers)) {
          this.props.eventHandlers.handleDelete(this.props.idx);
        }
        break;
      default:
        break;
    }
  };
  handleSetClick = (range: [number, number]): void => {
    window.location.href =
      Routes.DRAFTS_NEW +
      "/#" +
      this.props.factHash +
      "&" +
      range[0] +
      "&" +
      range[1] +
      window.location.pathname;
  };
  handleVideoEnd = (event: any) => {
    event.target.stopVideo();
  };
  render() {
    const { props } = this;

    let classes = "editor__video-container";

    return (
      <div
        tabIndex={0}
        className={classes}
        onClick={this.handleClick}
        onFocus={this.handleFocus}
        onKeyDown={this.handleKeyDown}
      >
        <Video
          onSetClick={this.handleSetClick}
          videoFact={this.props.videoFact}
          clipRange={this.props.range}
        />
      </div>
    );
  }
}

export default EditorVideoContainer;
