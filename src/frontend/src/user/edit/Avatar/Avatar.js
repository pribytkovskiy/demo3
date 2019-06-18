import React, {Component} from 'react'
import {Icon, Modal, Upload} from 'antd';
import {ACCESS_TOKEN, ONLINE_API_URL} from "../../../constants/constants";
import './Avatar.css'
import Cookie from "react-cookies";
import {renderAvatar} from "../../../util/api/APIUtils";

class Avatar extends Component {
  state = {
    previewVisible: false,
    previewImage: ``,
    fileList: [{
      uid: -1,
      name: 'avatar',
      status: 'done',
      url: '',
    }],
  };


  loadAvatar = (id) => {

    renderAvatar(id)
      .then(response => {
        this.setState({
          fileList: [{
            uid: -1,
            name: 'avatar',
            status: 'done',
            url: `${ONLINE_API_URL}/users/${id}/image`
          }],
        });
      }).catch(error => {
    });
  };


  componentDidMount() {
    const id = this.props.userId;
    this.loadAvatar(id);
  }



  handleCancel = () => this.setState({previewVisible: false})

  handlePreview = (file) => {
    this.setState({
      previewImage: file.url || file.thumbUrl,
      previewVisible: true,
    });
  };

  handleChange = ({fileList}) => this.setState({fileList})

  render() {
    const {previewVisible, previewImage, fileList} = this.state;
    const uploadButton = (
      <div>
        <Icon type="plus"/>
        <div className="ant-upload-text">Upload</div>
      </div>
    );

    return (
      <div className="clearfix">
        <Upload
          headers={{
            'Authorization': 'Bearer ' + localStorage.getItem(ACCESS_TOKEN),
            'X-XSRF-TOKEN': Cookie.load('XSRF-TOKEN'),
            'Accept': 'application/json',
          }}
          name="file"
          action={`${ONLINE_API_URL}/users/${this.props.userId}/image`}
          listType="picture-card"
          fileList={fileList}
          onPreview={this.handlePreview}
          onChange={this.handleChange}
          withCredentials={true}
        >
          {fileList.length >= 1 ? null : uploadButton}
        </Upload>
        <Modal visible={previewVisible} footer={null} onCancel={this.handleCancel}>
          <img alt="avatar" style={{width: '100%'}} src={previewImage}/>
        </Modal>
      </div>
    );
  }
}

export default Avatar