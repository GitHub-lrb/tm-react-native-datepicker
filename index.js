import {
    Platform,
    NativeModules,
    NativeAppEventEmitter,
    processColor,
    View,
    TouchableOpacity,
    StyleSheet
} from 'react-native';
import React, {Component} from 'react';

const ios = Platform.OS === 'ios';
const android = Platform.OS === 'android';

export default class TMDatePicker extends Component {

    static propTypes = {
        style: React.PropTypes.object,
        titleText: React.PropTypes.string,
        titleTextColor: React.PropTypes.string,
        placeholder:React.PropTypes.string,
        doneText: React.PropTypes.string,
        doneTextColor: React.PropTypes.string,
        cancelText: React.PropTypes.string,
        cancelTextColor: React.PropTypes.string,
        topBarColor: React.PropTypes.string,
        editable: React.PropTypes.bool,
    };


    static defaultProps = {
        style:{},
        titleText: '',
        titleTextColor: '#fff',
        placeholder: '请选择时间',
        doneText: '确定',
        doneTextColor: '#fff',
        cancelText: '取消',
        cancelTextColor: '#fff',
        topBarColor: '#263648',
        editable: true,
    };

    constructor(props){
        super(props);
        this.Picker = NativeModules.RNDatePicker;

    }


    render() {
        let textView = null;
        if (this.props.value.length === 0){
            textView = <Text style = {styles.placeholder}>{this.props.placeholder}</Text>
        }
        else{
            textView = <Text style = {styles.value}>{this.props.value}</Text>
        }
        let viewBackColor = 'white';
        if (!this.props.editable){
            viewBackColor = '#eee';
        }
        return (
            <View style = {[styles.viewStyle,this.props.style,{backgroundColor:viewBackColor}]}>
                <TouchableOpacity style = {styles.btn}
                                  disabled={!this.props.editable}
                                  onPress={this.showDatePicker}>
                    {textView}
                </TouchableOpacity>
            </View>
        )
    }

    showDatePicker = () => {
        let opt = {
            mode: 'date',
            ...this.props,
            titleTextColor: processColor(this.props.titleTextColor),
            doneTextColor: processColor(this.props.doneTextColor),
            cancelTextColor: processColor(this.props.cancelTextColor),
            topBarColor: processColor(this.props.topBarColor),
        };
        this.Picker.show(opt,this.props.onPickerConfirm);
    };
}


const styles = StyleSheet.create({
    viewStyle: {
        width: 120,
        height: 36,
        borderColor: '#c2c3c7',
        borderWidth: 1,
        borderRadius: 6
    },
    btn: {
        flex:1,
        justifyContent:'center',
    },

    placeholder: {
        color:'#c8c8c8',
        fontSize: 16
    },
    value:{
        color:'black',
        fontSize:16
    }
});

