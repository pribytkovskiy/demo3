import React, {Component} from "react";
import {Button, Card, Col, Row, Skeleton} from 'antd';
import Event from "../../events/Event/Event";
import Participants from "../../participants/Participants";
import {getEvent, getParticipantByEventId} from "../../util/api/APIUtils";
import {Link} from "react-router-dom";

class EventPage extends Component {

  state = {
    event: null,
    eventLoading: true,
    participants: [],
    participantsLoading: true,
  };

  componentDidMount() {
    getEvent(this.props.match.params.id)
      .then(response => {
        this.setState({
          event: response.data,
          eventLoading: false,
        });
      })
      .catch(reason => {
        this.setState({eventLoading: false});
        console.log(reason);
      });
    getParticipantByEventId(this.props.match.params.id)
      .then(response => {
        this.setState({
          participants: response.data,
          participantsLoading: false,
        })
      })
      .catch(reason => {
        this.setState({
          participantsLoading: false,
        });
        console.log(reason);
      })
  }

  render() {
    const eventControls = (this.props.currentUser && this.props.currentUser.grantedAuthorities.includes("MANAGER"))
      ? (
        <div style={{float: "right"}}>
          <Link to={this.props.match.params.id + "/edit"}>
            <Button>Edit</Button>
          </Link>
        </div>
      )
      : null;

    return (
      <div className="bc-content-body">
        <Row gutter={24}>
          <Col md={24} lg={12}>
            <Skeleton active loading={this.state.eventLoading} paragraph={4}>
              <Event data={this.state.event}/>
              <Card>
                <Link to={"/events/participate/" + +this.props.match.params.id}>
                  <Button type="default">Participate</Button>
                </Link>
                {eventControls}
              </Card>
            </Skeleton>
          </Col>
          <Col md={24} lg={12}>
            <Card title="Participants">
              <Skeleton active loading={this.state.participantsLoading} paragraph={5}>
                <Participants participants={this.state.participants} {...this.props}/>
              </Skeleton>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}

export default EventPage;
