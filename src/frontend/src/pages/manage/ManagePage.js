import React, {Component} from 'react';
import {Button, Card, Col, Row, Skeleton} from 'antd';
import Events from "../../events/Events";
import {getAllEvents} from "../../util/api/APIUtils";
import {Link} from "react-router-dom";

class ManagePage extends Component {
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
    const cardTitle = (
      <Link to="/events/new">
        <Button icon="plus">Add event</Button>
      </Link>);

    return (
      <div className="bc-content-body">
        <Row gutter={24}>
          <Col span={24}>
            <Skeleton loading={this.state.loading} paragraph={5} active>
              <Card title={cardTitle} bordered={false}>
                <Events events={this.state.events}/>
              </Card>
            </Skeleton>
          </Col>
        </Row>
      </div>
    );
  }
}

export default ManagePage;
