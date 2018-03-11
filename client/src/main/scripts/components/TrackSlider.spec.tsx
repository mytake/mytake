import * as React from "react";
import * as renderer from "react-test-renderer";
import {} from "jest";
import TrackSlider, { TrackSliderEventHandlers } from "./TrackSlider";
import {
  initialRangeSliders,
  unzoomedRangeSliders,
  zoomedRangeSliders
} from "../utils/testUtils";

const eventHandlers: TrackSliderEventHandlers = {
  onAfterRangeChange: jest.fn(),
  onRangeChange: jest.fn()
};

jest.mock("./RangeContainer", () => ({
  default: "RangeContainer"
}));

test("Initial render", () => {
  const tree = renderer
    .create(
      <TrackSlider
        start={0}
        end={5224}
        eventHandlers={eventHandlers}
        rangeSliders={initialRangeSliders}
        stateAuthority={"SCROLL"}
        step={1}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test("Zoomed render", () => {
  const tree = renderer
    .create(
      <TrackSlider
        start={4}
        end={82}
        eventHandlers={eventHandlers}
        rangeSliders={zoomedRangeSliders}
        stateAuthority={"SCROLL"}
        step={0.1}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test("Unzoomed render", () => {
  const tree = renderer
    .create(
      <TrackSlider
        start={4}
        end={82}
        eventHandlers={eventHandlers}
        rangeSliders={unzoomedRangeSliders}
        stateAuthority={"SCROLL"}
        step={0.1}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});
