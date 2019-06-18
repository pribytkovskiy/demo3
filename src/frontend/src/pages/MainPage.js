import React, {Component} from 'react';
import {Card, Row, Col, Skeleton} from 'antd';
import Events from "../events/Events";
import {getAllEvents} from "../util/api/APIUtils";

class MainPage extends Component {
  state = {
    events: [],
    loading: true,
  };

  componentWillMount() {
    getAllEvents()
      .then(response => {
        this.setState({
          events: response.data,
          loading: false,
        })
      })
      .catch(reason => {
        console.log(reason);
        this.setState({loading: false,});
      })
  }

  render() {
    return (
      <div className="main-page bc-content-body">
        <Row gutter={24}>
          <Col span={24}>
            <Skeleton loading={this.state.loading} paragraph={5} active>
              <Card
                bordered={false}>
                <Events events={this.state.events}/>
              </Card>
            </Skeleton>
          </Col>
        </Row>
      </div>
    );
  }
}

export default MainPage;
